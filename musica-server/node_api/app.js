var restify = require('restify');
var server = restify.createServer();
var setupController = require('./controllers/setupController.js');
var userController = require('./controllers/userController.js');
var postController = require('./controllers/postController.js');
var restifyValidator = require('restify-validator');
var config = require('./config/dbConnection.js');
var mongoose = require('mongoose');
var async_query = require('async');

mongoose.Promise = global.Promise;
mongoose.connect(config.getMongoConnection() ,function(err) {
  if (err)
    console.log(err);
});
setupController(server,restify,restifyValidator);
userController(server);
postController(server,async_query);

server.listen(22222,"192.168.0.5", function(){
  console.log('%s listening at %s', server.name, server.url);
});
