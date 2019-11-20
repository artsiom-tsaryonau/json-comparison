DROP TABLE IF EXISTS json_comparison;
CREATE TABLE json_comparison (
    comparison_id varchar(128) primary key,
    left_side varchar,
    right_side varchar,
    decision varchar(128),
    differences varchar
);
