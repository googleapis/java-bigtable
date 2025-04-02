use std::env;

const FIELD_VALUE_SIZE_ENV: &str = "FIELD_VALUE_SIZE";
const COLUMN_FAMILIES_ENV: &str = "COLUMN_FAMILIES";
const COLUMNS_PER_FAMILI_ENV: &str = "COLUMNS_PER_FAMILY";

const DELIMITER: &str = "|";
const VALUE_CHAR: &str = "0";

fn main() {
    let field_value_size: usize = get_env_var_u64(FIELD_VALUE_SIZE_ENV) as usize;
    let column_families: u64 = get_env_var_u64(COLUMN_FAMILIES_ENV);
    let columns_per_family: u64 = get_env_var_u64(COLUMNS_PER_FAMILI_ENV);

    let field_value: String = VALUE_CHAR.repeat(field_value_size);

    let (value_schema, value) = if column_families == 0 {
        string_schema_and_value(field_value)
    } else {
        struct_schema_and_value(column_families, columns_per_family, field_value)
    };
    let schema_and_value: String = format!("{{\"schema\":{},\"payload\":{}}}", value_schema, value);
    let mut i: u64 = 0;
    loop {
        println!("\"{}\"{}{}", i, DELIMITER, schema_and_value);
        i += 1;
    }
}

fn get_env_var_u64(name: &str) -> u64 {
    let missing_error: String = format!("Missing env var: {}.", name);
    let invalid_error: String = format!("Non-u64 env var: {}.", name);
    env::var(name)
        .expect(&missing_error)
        .parse::<u64>()
        .expect(&invalid_error)
}

fn struct_schema_and_value(
    column_families: u64,
    columns_per_family: u64,
    field_value: String,
) -> (String, String) {
    let columns: Vec<String> = (1..=columns_per_family)
        .map(|i| format!("c{}", i))
        .collect();
    let column_families: Vec<String> = (1..=column_families).map(|i| format!("cf{}", i)).collect();
    let column_family_schemas: Vec<String> = column_families
        .iter()
        .map(|cf| {
            format!(
                "{{\"type\":\"struct\",\"optional\":true,\"field\":\"{}\",\"fields\":[{}]}}",
                cf,
                columns
                    .iter()
                    .map(|c| format!(
                        "{{\"type\":\"string\",\"optional\":true,\"field\":\"{}\"}}",
                        c
                    ))
                    .collect::<Vec<String>>()
                    .join(",")
            )
        })
        .collect();
    let schema: String = format!(
        "{{\"name\":\"record\",\"type\":\"struct\",\"optional\":true,\"fields\":[{}]}}",
        column_family_schemas.join(",")
    );
    let value: String = format!(
        "{{{}}}",
        column_families
            .iter()
            .map(|cf| {
                format!(
                    "\"{}\":{{{}}}",
                    cf,
                    columns
                        .iter()
                        .map(|c| format!("\"{}\":\"{}\"", c, field_value))
                        .collect::<Vec<String>>()
                        .join(",")
                )
            })
            .collect::<Vec<String>>()
            .join(",")
    );

    (schema, value)
}

fn string_schema_and_value(field_value: String) -> (String, String) {
    (
        "{\"type\":\"string\",\"optional\":true}".to_string(),
        format!("\"{}\"", field_value),
    )
}
