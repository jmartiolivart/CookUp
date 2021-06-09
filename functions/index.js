const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//

exports.sendNotification1 = functions.firestore.document("recipes/{recipes}").onWrite((change, context) => {
  const payload = {
    notification: {
      title: "Updated Recipe ",
      body: "Recipe Updated or Added to FireBase",
    },
  };
  const getDeviceTokensPromise = admin.firestore().collection("users").get();
  return getDeviceTokensPromise.then(function(results) {
    const tokens = [];
    results.forEach((tokenDoc) => {
      tokens.push(tokenDoc.id);
    });
    return admin.messaging().sendToDevice(tokens, payload);
  });
});
