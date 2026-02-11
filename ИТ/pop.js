const users = [
{ id: 1, name: "Anna", age: 22, city: "Moscow", isActive: true },
{ id: 2, name: "Oleg", age: 17, city: "Kazan", isActive: false },
{ id: 3, name: "Ivan", age: 30, city: "Moscow", isActive: true },
{ id: 4, name: "Maria", age: 25, city: "Sochi", isActive: false }
];

// 1-----------------------------
function getActiveUsers(usersArray) {
return userArray.filter(user => user.isActive);
}

// 2-----------------------------
const getUserNames = (usersArray) => {
return userArray.map(user => user.name);
}

// 3-----------------------------
function findUserById(usersArray, id) {
    const foundUser = usersArray.find (user => user.id === id);
    return foundUser || null;
}

// 4-----------------------------
function getUsersStatistics(usersArray) {
    const total = usersArray.lenght;
    const active = usersArray.filter(user => user.isActive).lenght;
    return {
    total: total,
    active: active,
    inactive: total - active
  };
}

// 5-----------------------------
function getAverageAge(usersArray) {
  if (usersArray.length === 0) return 0;
  
  const totalAge = usersArray.reduce((sum, user) => sum + user.age, 0);
  return totalAge / usersArray.length;
}

// 6-----------------------------
function groupUsersByCity(usersArray) {
  return usersArray.reduce((result, user) => {
    if (!result[user.city]) {
      result[user.city] = [];
    }
    result[user.city].push(user);
    return result;
  }, {});
}