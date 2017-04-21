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
		var post_model = new PostModel();
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

  // route to update the user liked post
  server.put("/post/like/:id",function(req,res,next){
   req.assert('id','Id is required').notEmpty();

   var errors = req.validationErrors();
   if (errors) {
     helpers.failure(res,next,errors,404);
   }
   PostModel.findOne({ id: req.params.id }, function (err, post) {
     if(err) {
       helpers.failure(res,next,'Something went wrong while fetching from the database',500);
     }
     if(post === null){
       helpers.failure(res,next,'The specified user cannot be found in the database',404);
     }
     user_like.push(req.params.user_liked_email_address);
     post.save(function(err) {
       if(err) {
         helpers.failure(res,next,'The user cannot be added into the database',500);
       }
       else {
         helpers.success(res,next,user);
       }
     });
     //helpers.success(res,next,user);
   });
  });

  // route to update the user loved post
  server.put("/post/love/:id",function(req,res,next){
   req.assert('id','Id is required').notEmpty();

   var errors = req.validationErrors();
   if (errors) {
     helpers.failure(res,next,errors,404);
   }

   PostModel.findOne({ id: req.params.id }, function (err, post) {
     if(err) {
       helpers.failure(res,next,'Something went wrong while fetching from the database',500);
     }
     if(post === null){
       helpers.failure(res,next,'The specified user cannot be found in the database',404);
     }
     user_love.push(req.params.user_loved_email_address);
     post.save(function(err) {
       if(err) {
         helpers.failure(res,next,'The user cannot be added into the database',500);
       }
       else {
         helpers.success(res,next,user);
       }
     });
     //helpers.success(res,next,user);
   });
  });

  // route to get the posts of a user and the user following from the database (posts to be displayed on homeStream)
  server.get("/user/post/homestream/:email_address",function(req,res,next){
    req.assert('email_address','Email Address is required').notEmpty().isEmail();
    var errors = req.validationErrors();
    if (errors) {
      helpers.failure(res,next,errors[0],400);
    }
    PostModel.find({email_address: req.params.email_address }, function (err, posts) {
      if(err) {
        helpers.failure(res,next,'Something went wrong while fetching user from the database',500);
      }
      if(posts === null || posts.length === 0){
        helpers.failure(res,next,'This User haven\'t posted anything',404);
      }

      //find the user followed by finding its user following list
          UserModel.findOne({email_address: req.params.email_address }, function (err, user) {
          if(err) {
            helpers.failure(res,next,'Something went wrong while fetching user from the database',500);
          }
          if(user === null){
            helpers.failure(res,next,'The specified user cannot be found in the database',404);
          }
          else{
            var following_users=user.following;
            for (var i=0;i<following_users.length;i++){
                      PostModel.find({email_address: following_users[i] }, function (err, temp) {
                      if(err) {
                        helpers.failure(res,next,'Something went wrong while fetching user post from the database',500);
                      }
                      if(temp === null || temp.length ===0){
                        //helpers.failure(res,next,'This User haven\'t posted anything',404);
                      }
                      else {
                        posts.push(temp);
                      }
                    });           
            }
          }
        });
          helpers.success(res,next,posts);
    });
  });

}
