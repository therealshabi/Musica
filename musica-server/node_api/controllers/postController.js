var helpers = require('../config/helperFunctions.js');
var UserModel = require('../models/UserModel.js');
var PostModel = require('../models/PostModel.js');


module.exports = function(server){

  // route to get the posts of a particular user from the database
  server.get("/user/post/:email_address",function(req,res,next){
    req.assert('email_address','Email Address is required').notEmpty().isEmail();
    var errors = req.validationErrors();
    if (errors) {
      helpers.failure(res,next,errors[0],400);
    }
    PostModel.find({email_address: req.params.email_address }, function (err, posts) {
      if(err) {
        helpers.failure(res,next,'Something went wrong while fetching user from the database',500);
      }
      if(posts === null || posts.length ===0){
        helpers.failure(res,next,'This User haven\'t posted anything',404);
      }
      helpers.success(res,next,posts);
    });
  });

  // post route to post a post from a particular user
	server.post("/posts",function(req,res,next){
		// req.assert('email_address','Email address is required and must be a valid email ').notEmpty().isEmail();
		//
		// var errors = req.validationErrors();
		// if (errors) {
		// 	helpers.failure(res,next,errors,404);
		// }
		var post_model= new PostModel();
		post_model.email_address=req.params.email_address;
		post_model.post_title=req.params.post_title;
		post_model.post_info=req.params.post_info;
		post_model.post_genre_tag=req.params.post_genre_tag;
		post_model.user_like=[];
		post_model.user_love=[];
		post_model.post_album_pic=req.params.post_album_pic;
		post_model.save(function(err) {
			if(err){
				helpers.failure(res,next,err,500);
			}
      UserModel.findOne({email_address: req.params.email_address }, function (err, user) {
        if(err) {
          helpers.failure(res,next,'Something went wrong while fetching user from the database',500);
        }
        if(user === null){
          helpers.failure(res,next,'The specified user '+ req.params.email_address+' cannot be found in the database',404);
        }
        user.posts.push(post_model._id);
        user.save(function(err) {
          if(err) {
            helpers.failure(res,next,err,500);
          }
          else {
            helpers.success(res,next,post_model);
          }
        });
      });
		});
	});

}
