CREATE TABLE "joke" (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`title`	TEXT NOT NULL,
	`category_id`	INTEGER NOT NULL,
	`content`	TEXT NOT NULL,
	`is_viewed`	INTEGER NOT NULL DEFAULT 0
);
CREATE TABLE `favorite` (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`joke_id`	INTEGER NOT NULL
);
CREATE TABLE "category" (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`title`	TEXT NOT NULL,
	`parent_id`	INTEGER NOT NULL DEFAULT 0,
	`icon`  TEXT DEFAULT NULL
);