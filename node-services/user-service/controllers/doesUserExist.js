const db = require('../dbInstance');

// ResponseCode 1 -> User exists
// ResponseCode 0 -> User doesn't exists

const dbOperation = {
  doesUserExist: async (email) => {
    const userReference = db.collection('users').doc(email); 
    const object = await userReference.get();
    if (object.exists) {
      return { 'responseCode': '1' };
    } else {
      return { 'responseCode': '0' };
    }
  }
}

module.exports = dbOperation;