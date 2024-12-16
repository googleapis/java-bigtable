#!/bin/bash
set -e
# This script should be run at the root of the repository.
# This script is used to update googleapis commit to latest in generation
# configuration at the time of running and create a pull request.

# The following commands need to be installed before running the script:
# 1. git
# 2. gh

# The parameters of this script is:
# 1. base_branch, the base branch of the result pull request.
# 2. repo, organization/repo-name, e.g., googleapis/google-cloud-java
# 3. [optional] generation_config, the path to the generation configuration,
# the default value is generation_config.yaml in the repository root.
while [[ $# -gt 0 ]]; do
key="$1"
case "${key}" in
  --base_branch)
    base_branch="$2"
    shift
    ;;
  --repo)
    repo="$2"
    shift
    ;;
  --generation_config)
    generation_config="$2"
    shift
    ;;
  *)
    echo "Invalid option: [$1]"
    exit 1
    ;;
esac
shift
done

if [ -z "${base_branch}" ]; then
  echo "missing required argument --base_branch"
  exit 1
fi

if [ -z "${repo}" ]; then
  echo "missing required argument --repo"
  exit 1
fi

if [ -z "${generation_config}" ]; then
  generation_config="generation_config.yaml"
  echo "Use default generation config: ${generation_config}"
fi

current_branch="generate-libraries-${base_branch}"
title="chore: update googleapis commit at $(date)"

git checkout "${base_branch}"
# Try to find a open pull request associated with the branch
pr_num=$(gh pr list -s open -H "${current_branch}" -q . --json number | jq ".[] | .number")
# Create a branch if there's no open pull request associated with the
# branch; otherwise checkout the pull request.
if [ -z "${pr_num}" ]; then
  git checkout -b "${current_branch}"
  # Push the current branch to remote so that we can
  # compare the commits later.
  git push -u origin "${current_branch}"
else
  gh pr checkout "${pr_num}"
fi

# Only allow fast-forward merging; exit with non-zero result if there's merging
# conflict.
git merge -m "chore: merge ${base_branch} into ${current_branch}" "${base_branch}"

mkdir tmp-googleapis
# Use partial clone because only commit history is needed.
git clone --filter=blob:none https://github.com/googleapis/googleapis.git tmp-googleapis
pushd tmp-googleapis
git pull
latest_commit=$(git rev-parse HEAD)
popd
rm -rf tmp-googleapis
sed -i -e "s/^googleapis_commitish.*$/googleapis_commitish: ${latest_commit}/" "${generation_config}"

git add "${generation_config}"
changed_files=$(git diff --cached --name-only)
if [[ "${changed_files}" == "" ]]; then
    echo "The latest googleapis commit is not changed."
    echo "Skip committing to the pull request."
else
    git commit -m "${title}"
fi

# There are potentially at most two commits: merge commit and change commit.
# We want to exit the script if no commit happens (otherwise this will be an
# infinite loop).
# `git cherry` is a way to find whether the local branch has commits that are
# not in the remote branch.
# If we find any such commit, push them to remote branch.
unpushed_commit=$(git cherry -v "origin/${current_branch}" | wc -l)
if [[ "${unpushed_commit}" -eq 0 ]]; then
    echo "No unpushed commits, exit"
    exit 0
fi

if [ -z "${pr_num}" ]; then
  git remote add remote_repo https://cloud-java-bot:"${GH_TOKEN}@github.com/${repo}.git"
  git fetch -q remote_repo
  git push -f remote_repo "${current_branch}"
  gh pr create --title "${title}" --head "${current_branch}" --body "${title}" --base "${base_branch}"
else
  git push
fi
