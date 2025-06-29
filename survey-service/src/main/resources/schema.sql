--DROP TABLE IF EXISTS survey_responses;

CREATE TABLE survey_responses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    respondent_id VARCHAR(50) ,
    seq VARCHAR(50) NOT NULL,
    question VARCHAR(50) NOT NULL,
    answer VARCHAR(50) ,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);