INSERT INTO chapter(id, text) SELECT * FROM CSVREAD('target\classes\chapter.csv');
INSERT INTO button(id, text, smile, chapter_id) SELECT * FROM CSVREAD('target\classes\button.csv');
INSERT INTO button_redirect(button_id, redirect) SELECT * FROM CSVREAD('target\classes\redirect.csv');