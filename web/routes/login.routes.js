const express = require('express');
const router = express.Router();
const User = require('../models/UserModel')


router.get('/', function (req, res, next) {
    res.render('login', {
        title: 'Login',
        user: req.session.user,
        linkActive: 'login',
        err: undefined,
    });

});



router.post('/', function (req, res, next) {
    (async () => {
        let user = await User.fetchByUsername(req.body.user);

        //provjeri da li postoji user koji ima taj username
        if (user.user_name === undefined) {
            res.render('Login', {
                title: 'Login',
                linkActive: 'login',
                user: req.session.user,
                err: 'User does not exist or incorrect password!',
            });
            return;
        }

        //ako postoji username, provjeri da li lozinke odgovaraju
        if (user.checkPassword(req.body.password)) {
            req.session.user = user;
            res.redirect('/');
        } else {
            res.render('Login', {
                title: 'Login',
                linkActive: 'login',
                user: req.session.user,
                err: 'User does not exist or incorrect password!',
            });
        }

    })();

});


module.exports = router;
