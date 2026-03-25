-- 1. Clear existing test data (in reverse order to respect Foreign Keys)
DELETE
FROM game_items;
DELETE
FROM game_suspects;
DELETE
FROM games;
DELETE
FROM suspects;
DELETE
FROM items;
DELETE
FROM events;
DELETE
FROM players;

-- 2. Insert Player
INSERT INTO players (id, creation_date, deleted, username, password, token, full_name, contact_info)
VALUES (1, NOW(), false, 'detective_dan', 'pass123', 'mock-token-xyz', 'Dan Smith', 'dan@email.com');

-- 3. Insert Items
INSERT INTO items (id, creation_date, deleted, name, context)
VALUES (1, NOW(), false, 'Lead Pipe', 'Found in the Conservatory with strange markings.'),
       (2, NOW(), false, 'Revolver', 'Missing one bullet, smells like freshly fired powder.');

-- 4. Insert Suspects
INSERT INTO suspects (id, creation_date, deleted, name, background, pov, chat_history)
VALUES (1, NOW(), false, 'Colonel Mustard', 'A retired military officer with a quick temper.',
        'He claims he was in the dining room the whole time.',
        '{"role": "model", "parts": [{"text": "I was eating my roast beef, I heard nothing!"}]}'),
       (2, NOW(), false, 'Miss Scarlett', 'A glamorous actress who knew the victim well.',
        'She says she was powdering her nose in the lounge.',
        '{"role": "model", "parts": [{"text": "Darling, I despise violence. I was nowhere near the study."}]}');

-- 5. Insert Game
INSERT INTO games (id, creation_date, deleted, player_id, full_story, brief, status)
VALUES (1, NOW(), false, 1, 'Mr. Boddy was found dead in the study. You have 24 hours to find the killer.',
        'Murder at the Mansion', 0);

-- 6. Map Suspects to Game (Join Table)
INSERT INTO game_suspects (game_id, suspect_id)
VALUES (1, 1),
       (1, 2);

-- 7. Map Items to Game (Join Table)
INSERT INTO game_items (game_id, item_id)
VALUES (1, 1),
       (1, 2);