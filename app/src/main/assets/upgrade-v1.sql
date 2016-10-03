INSERT INTO `category` (id,title,parent_id,icon) VALUES (1,'Смешные',0,'my_category');
INSERT INTO `category` (id,title,parent_id,icon) VALUES (2,'Очень смешные',0,'my_category');
INSERT INTO `category` (id,title,parent_id) VALUES (3,'Эпично',0);
INSERT INTO `category` (id,title,parent_id) VALUES (4,'Новый русский',3);
INSERT INTO `category` (id,title,parent_id) VALUES (5,'Приколы про Россию',1);
INSERT INTO `category` (id,title,parent_id) VALUES (6,'Про студентов',1);
INSERT INTO `category` (id,title,parent_id) VALUES (7,'На парах',6);
INSERT INTO `category` (id,title,parent_id) VALUES (8,'После пар',6);



INSERT INTO `joke` (id,title,category_id,content,is_viewed) VALUES (1,'Прикол №1',7,'У меня не включается компьютер.
Я снял крышку, внутри какие-то микросхемы и мертвый паук.
Нужно купить нового паука? Я просто не разбираюсь.',0);
INSERT INTO `joke` (id,title,category_id,content,is_viewed) VALUES (2,'Прикол №2',7,'В дверях торчала записка: "Респект и уважуха, открыть не смогли".',0);
INSERT INTO `joke` (id,title,category_id,content,is_viewed) VALUES (3,'Прикол №3',7,'Женщина всегда догадывается о намерениях мужчины, о которых тот даже не подозревает.',0);
INSERT INTO `joke` (id,title,category_id,content,is_viewed) VALUES (4,'Прикол №4',8,'Вы что вчера делали?',0);
INSERT INTO `joke` (id,title,category_id,content,is_viewed) VALUES (5,'Прикол №5',8,'Объявление:
"Требуются образованные, но толковые работники".',0);
INSERT INTO `joke` (id,title,category_id,content,is_viewed) VALUES (6,'Прикол №6 (html)',8,'
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html" charset="UTF-8" />
        <link rel="stylesheet" type="text/css" href="default-theme.css">
    </head>
    <body>
        <h1>H1 Title</h1>
        <img src="http://risovach.ru/upload/2013/04/mem/petrosyanych_16294292_orig_.jpg" alt="" />
        <p><b>Люди шутят уже много лет. Шутят словами, строят рожи, рисуют картинки и снимают фильмы.
        Кто-то однажды должен был навести в этом порядок.
        Мы выбрали сто лучших шуток всех возможных жанров и расставили их в беспорядке. Наслаждайся!</b></p>
        <ul>
            <li>Пункт №1</li>
            <li>Пункт №2</li>
            <li>Пункт №3</li>
        </ul>
    </body>
</html>
',0);
