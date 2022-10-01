UPDATE tasks SET priority_id =
    (SELECT id FROM priorities WHERE name = 'high' LIMIT 1)
;