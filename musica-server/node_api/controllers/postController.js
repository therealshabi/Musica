var helpers = require('../config/helperFunctions.js');
var UserModel = require('../models/UserModel.js');
var PostModel = require('../models/PostModel.js');


module.exports = function(server, async_query){

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
   PostModel.findOne({ _id: req.params.id }, function (err, post) {
     if(err) {
       helpers.failure(res,next,'Something went wrong while fetching from the database',500);
     }
     if(post === null){
       helpers.failure(res,next,'The specified user cannot be found in the database',404);
     }
     post.user_like.push(req.params.user_liked_email_address);
     post.save(function(err) {
       if(err) {
         helpers.failure(res,next,'The user cannot be added into the database',500);
       }
       else {
         helpers.success(res,next,post);
       }
     });
   });
  });

  // route to update the user loved post
  server.put("/post/love/:id",function(req,res,next){
   req.assert('id','Id is required').notEmpty();

   var errors = req.validationErrors();
   if (errors) {
     helpers.failure(res,next,errors,404);
   }

   PostModel.findOne({ _id: req.params.id }, function (err, post) {
     if(err) {
       helpers.failure(res,next,'Something went wrong while fetching from the database',500);
     }
     if(post === null){
       helpers.failure(res,next,'The specified user cannot be found in the database',404);
     }
     post.user_love.push(req.params.user_loved_email_address);
     post.save(function(err) {
       if(err) {
         helpers.failure(res,next,'The user cannot be added into the database',500);
       }
       else {
         helpers.success(res,next,post);
       }
     });
   });
  });


  // route to update the user liked post (unliking the post)
  server.put("/post/unlike/:id",function(req,res,next){
   req.assert('id','Id is required').notEmpty();

   var errors = req.validationErrors();
   if (errors) {
     helpers.failure(res,next,errors,404);
   }
   PostModel.findOne({ _id: req.params.id }, function (err, post) {
     if(err) {
       helpers.failure(res,next,'Something went wrong while fetching from the database',500);
     }
     if(post === null){
       helpers.failure(res,next,'The specified user cannot be found in the database',404);
     }
     var index = post.user_like.indexOf(req.params.user_liked_email_address);
     post.user_like.splice(index,1);
     post.save(function(err) {
       if(err) {
         helpers.failure(res,next,'The user cannot be added into the database',500);
       }
       else {
         helpers.success(res,next,post);
       }
     });
   });
  });


  // route to update the user loved post (unloving the post)
  server.put("/post/unlove/:id",function(req,res,next){
   req.assert('id','Id is required').notEmpty();

   var errors = req.validationErrors();
   if (errors) {
     helpers.failure(res,next,errors,404);
   }
   PostModel.findOne({ _id: req.params.id }, function (err, post) {
     if(err) {
       helpers.failure(res,next,'Something went wrong while fetching from the database',500);
     }
     if(post === null){
       helpers.failure(res,next,'The specified user cannot be found in the database',404);
     }
     var index = post.user_love.indexOf(req.params.user_loved_email_address);
     post.user_love.splice(index,1);
     post.save(function(err) {
       if(err) {
         helpers.failure(res,next,'The user cannot be added into the database',500);
       }
       else {
         helpers.success(res,next,post);
       }
     });
   });
  });

  var calls = [];

  // HomeStreamPost Part-2
  // route to get the homeStream post of a particular user
  server.get("/user/homeStreamPost/:email_address",function(req,res,next){
    var result=[];
    req.assert('email_address','Email Address is required').notEmpty().isEmail();
    var errors = req.validationErrors();
    if (errors) {
      helpers.failure(res,next,errors[0],400);
    }

    UserModel.findOne({email_address: req.params.email_address }, function (err, user) {
      if(err) {
        helpers.failure(res,next,'Something went wrong while fetching user from the database',500);
      }
      if(user === null) {
        helpers.failure(res,next,'This user does not exist',404);
      }

      var f_posts = user.following_post;

      var pushDoc = function(item, callback) {
                if(item) {
                  PostModel.findOne({ _id: item}, function(err, post) {

                    if(post != null) {
                      result.push(post);
                      //Return to function which called this function
                      callback();
                    }
                    else callback();
                  });
                }
              };

//This function will call callback for each user following list to pushDoc function
              async_query.forEach(f_posts, pushDoc , function(err) {
                //err will be generated when finished traversing the error
                if(err)
                  console.log(err);
                  helpers.success(res,next,result);
              });

    });
});
}
