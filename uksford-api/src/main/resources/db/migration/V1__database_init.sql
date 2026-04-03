CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TYPE user_role AS ENUM ('ADMIN', 'USER');

-- Placeholder for academic_degree – you can add real values later with:
-- ALTER TYPE academic_degree ADD VALUE 'new_value' [ { BEFORE | AFTER } existing_value ];
CREATE TYPE academic_degree AS ENUM ('mgr.', 'dok.', 'dok. hab.', 'prof. uczelni', 'prof.',
    'mgr. inż', 'dok. inż', 'dok. hab. inż.');

-- Table: USERS ------------------------------------------------------
CREATE TABLE users (
                       user_id   UUID          PRIMARY KEY DEFAULT gen_random_uuid(),
                       email     VARCHAR(50)  NOT NULL,
                       password  VARCHAR(72)  NOT NULL,
                       nick      VARCHAR(50)  NOT NULL,
                       role      user_role    NOT NULL,
                       is_active boolean      NOT NULL DEFAULT true,
                       CONSTRAINT usr_email_uk UNIQUE (email),
                       CONSTRAINT usr_nick_uk UNIQUE (nick)
);

COMMENT ON COLUMN users.role IS 'Enum field';

-- Table: COURSES ----------------------------------------------------
CREATE TABLE courses (
                         course_id UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
                         code      CHAR(15)    NOT NULL,
                         name      VARCHAR(50) NOT NULL,
                         CONSTRAINT crs_code_uk UNIQUE (code)
);

-- Table: INSTRUCTORS ------------------------------------------------
CREATE TABLE instructors (
                             instructor_id   UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                             last_name       VARCHAR(30)     NOT NULL,
                             first_name      VARCHAR(30)     NOT NULL,
                             academic_degree academic_degree NOT NULL
);

COMMENT ON COLUMN instructors.academic_degree IS 'Enum field';

-- Table: REVIEWS ----------------------------------------------------
CREATE TABLE reviews (
                         review_id        UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
                         engagement       SMALLINT     NOT NULL,
                         hardness         SMALLINT     NOT NULL,
                         posted_at        TIMESTAMP    NOT NULL,
                         user_id          UUID         NOT NULL,
                         course_id        UUID         NOT NULL,
                         content          VARCHAR(1000),
                         original_content VARCHAR(1000),
                         updated_at       TIMESTAMP,
                         CONSTRAINT rev_usr_fk FOREIGN KEY (user_id) REFERENCES users (user_id),
                         CONSTRAINT rev_crs_fk FOREIGN KEY (course_id) REFERENCES courses (course_id),
                         CONSTRAINT rev_eng_val_chk CHECK (engagement BETWEEN 0 AND 100),
                         CONSTRAINT rev_hard_val_chk CHECK (hardness BETWEEN 0 AND 100)
);

COMMENT ON COLUMN reviews.content          IS 'Users'' opinion about course.';
COMMENT ON COLUMN reviews.original_content IS 'The value is set if content has been updated. After first change stays the same.';

CREATE INDEX rev_usr_fk ON reviews (user_id);
CREATE INDEX rev_crs_fk_idx ON reviews (course_id);

-- Table: COMMENTS ---------------------------------------------------
CREATE TABLE comments (
                          comment_id        UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
                          content           VARCHAR(800) NOT NULL,
                          user_id           UUID         NOT NULL,
                          review_id         UUID         NOT NULL,
                          posted_at         TIMESTAMP    NOT NULL,
                          parent_comment_id UUID,
                          updated_at        TIMESTAMP,
                          original_content  VARCHAR(800),
                          CONSTRAINT com_com_fk FOREIGN KEY (parent_comment_id) REFERENCES comments (comment_id),
                          CONSTRAINT com_rev_fk FOREIGN KEY (review_id) REFERENCES reviews (review_id),
                          CONSTRAINT com_usr_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
);

COMMENT ON TABLE comments IS 'Comments about reviews left by other users.';
COMMENT ON COLUMN comments.original_content IS 'The value is set if content has been updated. After first change stays the same.';

CREATE INDEX com_com_fk_idx ON comments (parent_comment_id);
CREATE INDEX com_rev_fk_idx ON comments (review_id);
CREATE INDEX com_usr_fk_idx ON comments (user_id);

-- Table: CONDUCTED_CLASSES ------------------------------------------
CREATE TABLE conducted_classes (
                                   review_id     UUID  NOT NULL,
                                   instructor_id UUID  NOT NULL,
                                   CONSTRAINT cdcls_pk PRIMARY KEY (review_id, instructor_id),
                                   CONSTRAINT cdcls_rev_fk FOREIGN KEY (review_id) REFERENCES reviews (review_id),
                                   CONSTRAINT cdcls_inst_fk FOREIGN KEY (instructor_id) REFERENCES instructors (instructor_id)
);

COMMENT ON TABLE conducted_classes IS 'Reviewed course can have multiple instructors, as well as one instructor can conduct multiple courses.';

CREATE INDEX cdcls_inst_fk_idx ON conducted_classes (instructor_id);

-- Table: VOTES ------------------------------------------------------
CREATE TABLE votes (
                       user_id   UUID    NOT NULL,
                       review_id UUID    NOT NULL,
                       is_useful BOOLEAN NOT NULL,
                       CONSTRAINT vt_pk PRIMARY KEY (review_id, user_id),
                       CONSTRAINT vt_usr_fk FOREIGN KEY (user_id) REFERENCES users (user_id),
                       CONSTRAINT vt_rev_fk FOREIGN KEY (review_id) REFERENCES reviews (review_id)
);

COMMENT ON TABLE votes IS 'Users leave positive or negative vote about review. If this record doesn''t exist for concrete user-review pair, that means concrete user didn''t leave vote for a review.';
COMMENT ON COLUMN votes.is_useful IS 'true - review was useful false - review was not useful';

-- =====================================================
-- Triggers to prevent updating foreign key columns
-- (equivalent to Oracle's FKNTM triggers)
-- =====================================================

-- Trigger for COMMENTS
CREATE OR REPLACE FUNCTION fkntm_comments() RETURNS TRIGGER AS $$
BEGIN
    IF OLD.user_id != NEW.user_id OR OLD.review_id != NEW.review_id THEN
        RAISE EXCEPTION 'Non Transferable FK constraint on table COMMENTS is violated';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER fkntm_comments
    BEFORE UPDATE OF user_id, review_id ON comments
    FOR EACH ROW
EXECUTE FUNCTION fkntm_comments();

-- Trigger for REVIEWS
CREATE OR REPLACE FUNCTION fkntm_reviews() RETURNS TRIGGER AS $$
BEGIN
    IF OLD.user_id != NEW.user_id OR OLD.course_id != NEW.course_id THEN
        RAISE EXCEPTION 'Non Transferable FK constraint on table REVIEWS is violated';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER fkntm_reviews
    BEFORE UPDATE OF user_id, course_id ON reviews
    FOR EACH ROW
EXECUTE FUNCTION fkntm_reviews();

-- Trigger for VOTES
CREATE OR REPLACE FUNCTION fkntm_votes() RETURNS TRIGGER AS $$
BEGIN
    IF OLD.user_id != NEW.user_id OR OLD.review_id != NEW.review_id THEN
        RAISE EXCEPTION 'Non Transferable FK constraint on table VOTES is violated';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER fkntm_votes
    BEFORE UPDATE OF user_id, review_id ON votes
    FOR EACH ROW
EXECUTE FUNCTION fkntm_votes();
