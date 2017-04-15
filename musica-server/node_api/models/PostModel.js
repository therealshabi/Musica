var mongoose = require('mongoose');

var Schema = mongoose.Schema,
    ObjectId = Schema.ObjectId;

var PostSchema = new Schema({
    id    : ObjectId,
    email_address     : String,
    post_title     : String,
    post_info     : String,
    post_genre_tag     : String,
    user_like     : [String],
    user_love     : [String],
    post_album_pic     : String
});

// users here is the collection(table) name that we are
// going to create in mlab database
var PostModel = mongoose.model('posts', PostSchema);

module.exports = PostModel;
