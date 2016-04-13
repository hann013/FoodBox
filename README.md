# FoodBox
Track the food in your fridge and use it up before it expires!

### Inspiration
Nobody likes wasted food! 
That's why we decided to make a mobile app to keep track of your perishables. 

### Demo
[YouTube Video](https://www.youtube.com/watch?v=1TjB7Ww69VQ)

We leveraged API's to help address these **challenges** :

1. **Inputting food is tedious** - Our app can label your food from a simple snap of the camera!
2. **Extra incentives for tracking your food** - Healthy recipe suggestions are given based on the perishing food item!
3. **Social Interaction** - View your friend's perishing food, and collaborate together!

### What it does
* Keeps track of your perishable food items and lets you know when they are about to expire.
* Discover healthy recipes for food in your inventory.
* See your friend's published food items, and collaborate with them on recipes.
* Input food items quickly by taking a picture and having the app label the food.
* Keeps you healthy by alerting you when your fridge becomes unhealthy.

### How we built it
* Android Studio.
* Firebase for the backend to store data and provide real-time syncing of food items with friends.
* Clarifai API to leverage deep-learning networks to label a food item from your camera.
* Edamam API to generate healthy recipes based on your perishing items.

### Challenges we ran into
* Coding for a real-time database requires some sacrifice when designing the schema.
* Getting Android UI to work nicely with us.

### Accomplishments that we're proud of
* A working application with real-time data syncing between friends.
* Image recognition on a mobile phone.
* Generating food recipes based on healthy tags.

### What we learned
* Firebase is easy to learn but hard to master. But the real-time data integration on Android took a lot work off our plates.
* Machine learning for food identification is viable, but in the end it often requires human validation.

### What's next for FoodBox
* With some minor changes to the UI, some small tweaks to the database and contacts listings, we would be ready to launch into an app store.
