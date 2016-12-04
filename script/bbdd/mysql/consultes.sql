SELECT
    llo.codi,
    sub.nom,
    fac.numero,
    fac.data,
    fac.import,
    fac.estat,
    fac.id
FROM
	factura fac,
    submin sub,
    lloguer llo
WHERE
	llo.codi = 'GUILLEM'
and sub.lloguer_id = llo.id
and fac.subministrament_id = sub.id
order by
    fac.data desc
