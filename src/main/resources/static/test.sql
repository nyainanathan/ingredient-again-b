SELECT id_ingredient,unit, 
SUM( CASE 
    WHEN type = 'IN' THEN quantity
    WHEN type = 'OUT' THEN -quantity
    ELSE 0 END
    ) as qte
FROM stockmovement
WHERE id_ingredient = 1 AND creation_datetime <= '2024-01-06 12:00:00'
GROUP BY id_ingredient, unit;


SELECT id, quantity, unit, type, creation_datetime
FROM stockmovement
WHERE id_ingredient = 1 AND creation_datetime < '2024-01-06 12:00:00'