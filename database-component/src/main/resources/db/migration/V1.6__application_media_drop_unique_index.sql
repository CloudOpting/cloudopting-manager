DROP INDEX application_media_application_i;

CREATE INDEX application_media_application_i
  ON application_media
  USING btree
  (application_id);
