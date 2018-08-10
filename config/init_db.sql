CREATE TABLE resume (
  uuid      CHAR(36) PRIMARY KEY NOT NULL,
  full_name TEXT                 NOT NULL
);

CREATE TABLE contact (
  id          SERIAL,
  resume_uuid CHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
  type        TEXT     NOT NULL,
  value       TEXT     NOT NULL
);
CREATE UNIQUE INDEX contact_uuid_type_index
  ON contact (resume_uuid, type);


CREATE TABLE plain_text (
  id          SERIAL,
  plain_resume_uuid CHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
  plain_type        TEXT     NOT NULL,
  plain_value       TEXT     NOT NULL
);
CREATE UNIQUE INDEX plain_text_uuid_type_index
  ON plain_text (plain_resume_uuid, plain_type);

