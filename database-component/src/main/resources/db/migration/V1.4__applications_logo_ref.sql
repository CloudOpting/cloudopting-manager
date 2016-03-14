--ALTER TABLE applications ADD COLUMN application_logo_ref text;
COMMENT ON COLUMN applications.application_logo_ref IS 'The JackRabbit Path to the Application Logo file';