const express = require('express');
const router = express.Router();
const authHandler = require('./helpers/auth-handler');
const Setting = require("../models/SettingModel");


router.get('/display/:id', authHandler, async function (req, res, next) {
    let gameCodeSettings;

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


router.get('/createNewRelativeNumberTask', authHandler, async function (req, res, next) {
    res.render('relativeNumberTask', {
        title: 'MathSpace! New relative number task!',
        settingsId: req.session.game_code,
        user: req.session.user,
        linkActive: 'user'
    });

});


router.post('/createNewRelativeNumberTask', authHandler, async function (req, res, next) {
    let objekt = "";
    objekt = objekt + "taskObjectType:RelativeNumberTask;";
    objekt = objekt + "taskText:" + req.body.taskText + ";";
    objekt = objekt + "taskType:" + req.body.taskType + ";";
    objekt = objekt + "minNumber:" + req.body.minNumber + ";";
    objekt = objekt + "maxNumber:" + req.body.maxNumber + ";";
    objekt = objekt + "relativeNumber:" + req.body.relativeNumber + ";";
    if (req.body.allowRelativeNumberChange === undefined) {
        objekt = objekt + "allowRelativeNumberChange:0";
    } else {
        objekt = objekt + "allowRelativeNumberChange:1";
    }

    await (async () => {
        await Setting.createNewTask(req.body.taskText, objekt, req.session.game_code)
    })();

    res.redirect("/gamecode/display/" + req.session.game_code)
});

router.get('/createNewUnrelativeNumberTask', authHandler, async function (req, res, next) {
    res.render('unrelativeNumberTask', {
        title: 'MathSpace! New unrelative number task!',
        settingsId: req.session.game_code,
        user: req.session.user,
        linkActive: 'user'
    });

});

router.post('/createNewUnrelativeNumberTask', authHandler, async function (req, res, next) {
    let objekt = "";
    objekt = objekt + "taskObjectType:UnrelativeNumberTask;";
    objekt = objekt + "taskText:" + req.body.taskText + ";";
    objekt = objekt + "taskType:" + req.body.taskType + ";";
    objekt = objekt + "minNumber:" + req.body.minNumber + ";";
    objekt = objekt + "maxNumber:" + req.body.maxNumber;


    await (async () => {
        await Setting.createNewTask(req.body.taskText, objekt, req.session.game_code)
    })();

    res.redirect("/gamecode/display/" + req.session.game_code)
});


router.get('/createNewShapeTask', authHandler, async function (req, res, next) {
    res.render('shapeTask', {
        title: 'MathSpace! New shape number task!',
        settingsId: req.session.game_code,
        user: req.session.user,
        linkActive: 'user'
    });
});

router.post('/createNewShapeTask', authHandler, async function (req, res, next) {
    let objekt = "";
    objekt = objekt + "taskObjectType:ShapeTask;";
    objekt = objekt + "taskText:" + req.body.taskText + ";";
    objekt = objekt + "taskType:SHAPE;";
    objekt = objekt + "askedShape:" + req.body.askedShape;

    await (async () => {
        await Setting.createNewTask(req.body.taskText, objekt, req.session.game_code)
    })();

    res.redirect("/gamecode/display/" + req.session.game_code)
});


router.get('/createNewWordsTask', authHandler, async function (req, res, next) {
    res.render('wordsTask', {
        title: 'MathSpace! New words number task!',
        settingsId: req.session.game_code,
        user: req.session.user,
        linkActive: 'user'
    });
});

router.post('/createNewWordsTask', authHandler, async function (req, res, next) {
    let objekt = "";
    objekt = objekt + "taskObjectType:WordsTask;";
    objekt = objekt + "taskText:" + req.body.taskText + ";";
    objekt = objekt + "taskType:WORDCONTAINED;";
    objekt = objekt + "correctWords:" + req.body.correctWords + ";";
    objekt = objekt + "incorrectWords:" + req.body.incorrectWords;

    await (async () => {
        await Setting.createNewTask(req.body.taskText, objekt, req.session.game_code)
    })();

    res.redirect("/gamecode/display/" + req.session.game_code)
});


router.get('/remove/:id', authHandler, async function (req, res, next) {

    await (async () => {
        await Setting.removeTask(req.session.user.user_name, req.params.id);
    })();

    res.redirect('/gamecode/display/' + req.session.game_code);
});


//========================================================MOBITEL=======================================================
router.get('/mobile/settings/:id', async function (req, res, next) {
    let gameCodeSettings;

    await (async () => {
        gameCodeSettings = await Setting.getGameCodeSettings(req.params.id);
    })();

    return res.send(gameCodeSettings);
});

module.exports = router;
