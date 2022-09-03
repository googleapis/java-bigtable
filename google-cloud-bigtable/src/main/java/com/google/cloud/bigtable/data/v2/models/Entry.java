/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.bigtable.data.v2.models;

import com.google.api.core.InternalExtensionOnly;

/**
 * Default representation of a mod in a data change, which can be a {@link DeleteFamily}, a {@link
 * DeleteCells}, or a {@link SetCell} This class will be used by {@link ChangeStreamMutation} to
 * represent a list of mods in a logical change stream mutation.
 */
@InternalExtensionOnly
public interface Entry {}