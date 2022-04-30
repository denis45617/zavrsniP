const express = require('express');
const router = express.Router();
const authHandler = require('./helpers/auth-handler');
const Setting = require("../models/SettingModel");


router.get('/display/:id', authHandler, async function (req, res, next) {
    let gameCodeSettings;

    //TODO: provjeri da li ovaj id pripada ovom korisniku
    //i ako pripada onda stavi ovo...
    req.session.game_code = req.params.id;

    await (async () => {
        gameCodeSettings = await Setting.getGameCodeSettings(req.params.id);
    })();

    res.render('gamecode', {
        title: 'MathSpace! Game settings!',
        settingsId: req.session.game_code,
        settings: gameCodeSettings,
        user: req.session.user,
        linkActive: 'user'
    });
});

router.get('/editsetting/:id', authHandler, async function (req, res, next) {
    let gameCodeSettings;

    //TODO: provjeri da li ovaj id pripada ovom korisniku
    //i ako pripada onda stavi ovo...
    req.session.game_code = req.params.id;

    await (async () => {
        gameCodeSettings = await Setting.getGameCodeSettings(req.params.id);
    })();

    res.render('Setting', {
        title: 'User profile',
        settingsId: req.session.game_code,
        settings: gameCodeSettings,
        user: req.session.user,
        linkActive: 'user'
    });
});

router.get('/createnew', authHandler, async function (req, res, next) {
    await (async () => {
        await Setting.createNewTask("opis", "text", req.session.game_code)
    })();


    res.render('task', {
        title: 'New task',
        settingsId: req.session.game_code,
        user: req.session.user,
        linkActive: 'user'
    });
});


router.get('/createnew2', authHandler, async function (req, res, next) {
    await (async () => {
        await Setting.createNewTask("opis", "text", req.session.game_code)
    })();


    res.redirect('/gamecode/display/'+req.session.game_code);
});
router.get('/remove/:id', authHandler, async function (req, res, next) {

    await (async () => {
        await Setting.removeTask(req.session.user.user_name, req.params.id);
    })();

    res.redirect('/gamecode/display/' + req.session.game_code);
});

module.exports = router;
