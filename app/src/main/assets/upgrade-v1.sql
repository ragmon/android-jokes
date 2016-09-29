INSERT INTO `category` (id,title,parent_id) VALUES (1,'Смешные',0);
INSERT INTO `category` (id,title,parent_id) VALUES (2,'Очень смешные',0);
INSERT INTO `category` (id,title,parent_id) VALUES (3,'Эпично',0);
INSERT INTO `category` (id,title,parent_id) VALUES (4,'Новый русский',3);
INSERT INTO `category` (id,title,parent_id) VALUES (5,'Приколы про Россию',1);
INSERT INTO `category` (id,title,parent_id) VALUES (6,'Про студентов',1);
INSERT INTO `category` (id,title,parent_id) VALUES (7,'На парах',6);
INSERT INTO `category` (id,title,parent_id) VALUES (8,'После пар',6);


INSERT INTO `joke` (id,title,category_id,content) VALUES (1,'Прикол №1',7,'У меня не включается компьютер.
Я снял крышку, внутри какие-то микросхемы и мертвый паук.
Нужно купить нового паука? Я просто не разбираюсь.');
INSERT INTO `joke` (id,title,category_id,content) VALUES (2,'Прикол №2',7,'В дверях торчала записка: "Респект и уважуха, открыть не смогли".');
INSERT INTO `joke` (id,title,category_id,content) VALUES (3,'Прикол №3',7,'Женщина всегда догадывается о намерениях мужчины, о которых тот даже не подозревает.');
INSERT INTO `joke` (id,title,category_id,content) VALUES (4,'Прикол №4',8,'Вы что вчера делали?');
INSERT INTO `joke` (id,title,category_id,content) VALUES (5,'Прикол №5',8,'Объявление:
"Требуются образованные, но толковые работники".');