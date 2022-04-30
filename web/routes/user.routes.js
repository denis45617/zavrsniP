const express = require('express');
const router = express.Router();
const authHandler = require('./helpers/auth-handler');
const GameCodeModel = require("../models/GameCodeModel");
const db = require('../db')


router.get('/', authHandler, async function (req, res, next) {
    let resultGameSettings;
   //dohvati kodove za korisnika
    await (async () => {
         resultGameSettings = await GameCodeModel.getUserGameCodes(req.session.user.user_name);
    })();

        res.render('user', {
            title: 'User profile',
            codes: resultGameSettings,
            user: req.session.user,
            linkActive: 'user'
        });
});

router.get('/createnew', authHandler, async function (req, res, next) {
    let resultGameSettings;
    //dohvati kodove za korisnika

    await (async () => {
        await GameCodeModel.createNewGameCode(req.session.user.user_name);
    })();



    res.redirect('/user');
});

router.get('/remove/:id', authHandler, async function (req, res, next) {
    let resultGameSettings;
    //dohvati kodove za korisnika

    await (async () => {
        await GameCodeModel.removeGameCode(req.session.user.user_name, req.params.id);
    })();



    res.redirect('/user');
});

module.exports = router;
