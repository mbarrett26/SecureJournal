INSERT INTO  AUTHORITIES (ID, AUTHORITY)
    VALUES (0, 'ROLE_ANONYMOUS');
INSERT INTO  AUTHORITIES (ID, AUTHORITY)
    VALUES (1, 'ROLE_ADMIN');
INSERT INTO  AUTHORITIES (ID, AUTHORITY)
    VALUES (2, 'ROLE_USER');


INSERT into USERS (ID, PASSWORD, USERNAME, ENABLED)
    VALUES (0, '$2a$10$Em7KE6jS/CEQgzdYpdBqLu4.Q76KCLbhbNl.KwunXEj2Iy3SpjDxS', 'user', true);

INSERT INTO entries (date, text, img, active, userID) VALUES
  ('2010-03-07', 'Today I had ice cream', 'https://minimalistbaker.com/wp-content/uploads/2015/06/AMAZING-Vegan-Cherry-Pie-ICE-CREAM-10-ingredients-simple-methods-SO-delicious-vegan-recipe-icecream-dessert-cherry-summer-fruit-675x1024-1.jpg', true, 10),
  ('2010-08-03', 'Today I went to the beach', 'https://images.photowall.com/products/50554/tropical-beach-scene.jpg?h=699&q=85', true,2);