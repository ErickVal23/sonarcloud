CREATE TABLE "PROJECTS"(
    "UUID" CHARACTER VARYING(40) NOT NULL,
    "KEE" CHARACTER VARYING(400) NOT NULL,
    "QUALIFIER" CHARACTER VARYING(10) NOT NULL,
    "NAME" CHARACTER VARYING(2000),
    "DESCRIPTION" CHARACTER VARYING(2000),
    "PRIVATE" BOOLEAN NOT NULL,
    "TAGS" CHARACTER VARYING(500),
    "CREATED_AT" BIGINT,
    "UPDATED_AT" BIGINT NOT NULL,
    "NCLOC" BIGINT
);
ALTER TABLE "PROJECTS" ADD CONSTRAINT "PK_NEW_PROJECTS" PRIMARY KEY("UUID");
CREATE UNIQUE INDEX "UNIQ_PROJECTS_KEE" ON "PROJECTS"("KEE" NULLS FIRST);
CREATE INDEX "IDX_QUALIFIER" ON "PROJECTS"("QUALIFIER" NULLS FIRST);

CREATE TABLE "PROJECT_BRANCHES"(
    "UUID" CHARACTER VARYING(50) NOT NULL,
    "PROJECT_UUID" CHARACTER VARYING(50) NOT NULL,
    "KEE" CHARACTER VARYING(255) NOT NULL,
    "BRANCH_TYPE" CHARACTER VARYING(12) NOT NULL,
    "MERGE_BRANCH_UUID" CHARACTER VARYING(50),
    "PULL_REQUEST_BINARY" BINARY LARGE OBJECT,
    "MANUAL_BASELINE_ANALYSIS_UUID" CHARACTER VARYING(40),
    "CREATED_AT" BIGINT NOT NULL,
    "UPDATED_AT" BIGINT NOT NULL,
    "EXCLUDE_FROM_PURGE" BOOLEAN DEFAULT FALSE NOT NULL,
    "NEED_ISSUE_SYNC" BOOLEAN NOT NULL
);
ALTER TABLE "PROJECT_BRANCHES" ADD CONSTRAINT "PK_PROJECT_BRANCHES" PRIMARY KEY("UUID");
CREATE UNIQUE INDEX "UNIQ_PROJECT_BRANCHES" ON "PROJECT_BRANCHES"("BRANCH_TYPE" NULLS FIRST, "PROJECT_UUID" NULLS FIRST, "KEE" NULLS FIRST);

CREATE TABLE "LIVE_MEASURES"(
    "UUID" CHARACTER VARYING(40) NOT NULL,
    "PROJECT_UUID" CHARACTER VARYING(50) NOT NULL,
    "COMPONENT_UUID" CHARACTER VARYING(50) NOT NULL,
    "METRIC_UUID" CHARACTER VARYING(40) NOT NULL,
    "VALUE" DOUBLE PRECISION,
    "TEXT_VALUE" CHARACTER VARYING(4000),
    "MEASURE_DATA" BINARY LARGE OBJECT,
    "UPDATE_MARKER" CHARACTER VARYING(40),
    "CREATED_AT" BIGINT NOT NULL,
    "UPDATED_AT" BIGINT NOT NULL
);
ALTER TABLE "LIVE_MEASURES" ADD CONSTRAINT "PK_LIVE_MEASURES" PRIMARY KEY("UUID");
CREATE INDEX "LIVE_MEASURES_PROJECT" ON "LIVE_MEASURES"("PROJECT_UUID" NULLS FIRST);
CREATE UNIQUE INDEX "LIVE_MEASURES_COMPONENT" ON "LIVE_MEASURES"("COMPONENT_UUID" NULLS FIRST, "METRIC_UUID" NULLS FIRST);

CREATE TABLE "METRICS"(
    "UUID" CHARACTER VARYING(40) NOT NULL,
    "NAME" CHARACTER VARYING(64) NOT NULL,
    "DESCRIPTION" CHARACTER VARYING(255),
    "DIRECTION" INTEGER DEFAULT 0 NOT NULL,
    "DOMAIN" CHARACTER VARYING(64),
    "SHORT_NAME" CHARACTER VARYING(64),
    "QUALITATIVE" BOOLEAN DEFAULT FALSE NOT NULL,
    "VAL_TYPE" CHARACTER VARYING(8),
    "ENABLED" BOOLEAN DEFAULT TRUE,
    "WORST_VALUE" DOUBLE PRECISION,
    "BEST_VALUE" DOUBLE PRECISION,
    "OPTIMIZED_BEST_VALUE" BOOLEAN,
    "HIDDEN" BOOLEAN,
    "DELETE_HISTORICAL_DATA" BOOLEAN,
    "DECIMAL_SCALE" INTEGER
);
ALTER TABLE "METRICS" ADD CONSTRAINT "PK_METRICS" PRIMARY KEY("UUID");
CREATE UNIQUE INDEX "METRICS_UNIQUE_NAME" ON "METRICS"("NAME" NULLS FIRST);
