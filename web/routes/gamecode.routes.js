const express = require('express');
const router = express.Router();
const authHandler = require('./helpers/auth-handler');
const Setting = require("../models/SettingModel");
const GameResult = require("../models/ResultModel");
const bodyParser = require('body-parser');
const User = require("../models/UserModel");
router.use(bodyParser.json());
router.use(bodyParser.urlencoded({extended: true}));


router.get('/display/:id', authHandler, async function (req, res, next) {
    let gameCodeSettings;
    let results;

    if (!await User.checkUserHasGameCode(req.session.user.user_name, req.params.id)) {
        return res.redirect("/login");
    }

    req.session.game_code = req.params.id;


    await (async () => {
        gameCodeSettings = await Setting.getGameCodeSettings(req.params.id);
    })();

    await (async () => {
        results = await GameResult.getResults(req.params.id);
    })();

    res.render('gamecode', {
        title: 'MathSpace! Game settings!',
        settingsId: req.session.game_code,
        settings: gameCodeSettings,
        results: results,
        user: req.session.user,
        linkActive: 'user'
    });
});

router.get('/display/details/:id', authHandler, async function (req, res, next) {
    let result_id = req.params.id;
    let logs;

    console.log(req.session.game_code)


    await (async () => {
        logs = await GameResult.fetchById(result_id);

        if (req.session.game_code !== logs.game_code)
            return res.redirect("/login");
    })();

    logs.result = JSON.parse(logs.result);


    res.render('logs', {
        title: 'MathSpace! Logs!',
        settingsId: req.session.game_code,
        logs: logs,
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
    if (!await User.checkUserHasGameCode(req.session.user.user_name, req.session.game_code)) {
        return res.redirect("/login");
    }

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
    if (!await User.checkUserHasGameCode(req.session.user.user_name, req.session.game_code)) {
        return res.redirect("/login");
    }

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
    if (!await User.checkUserHasGameCode(req.session.user.user_name, req.session.game_code)) {
        return res.redirect("/login");
    }

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
    if (!await User.checkUserHasGameCode(req.session.user.user_name, req.session.game_code)) {
        return res.redirect("/login");
    }

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
        gameCodeSettings = await Setting.getGameCodeSettingsMobile(req.params.id);
    })();

    return res.send(gameCodeSettings);
});


router.post('/mobile/result', async function (req, res, next) {
    let highscore = req.body.highscore;
    let player_nickname = req.body.player_nickname;
    let game_code = req.body.game_code;
    let result = JSON.stringify(req.body.result);


    await (async () => {
        await GameResult.saveResult(highscore, player_nickname, result, game_code)
    })();

    return res.send("OK");
});

router.get('/mobile/leaderboard/:id', async function (req, res, next) {
    let leaderboard;

    await (async () => {
        leaderboard = await GameResult.getLeaderboardMobile(req.params.id);
    })();

    return res.send(leaderboard);
});


module.exports = router;
