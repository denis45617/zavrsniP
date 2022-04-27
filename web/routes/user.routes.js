const express = require('express');
const router = express.Router();
const authHandler = require('./helpers/auth-handler');

//prikaz podataka o korisniku (podaci o korisniku, adrese, narudžbe)
// Ulančavanje funkcija međuopreme
router.get('/', authHandler, function (req, res, next) {
    (async () => {

        res.render('user', {
            title: 'User profile',
            user: req.session.user,
            linkActive: 'user'
        });
    })()
});

module.exports = router;
