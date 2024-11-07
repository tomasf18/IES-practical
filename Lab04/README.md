# 112981

# Lab 4: Objective of this lab

    - Develop web projects with Spring Boot. 
    - Create and persist entities into a relational database using Spring Data.
    - Deploy Spring Boot application in Docker.

## Table of Contents




## Create React App

```bash
npx create-react-app my-app
cd my-app
npm start
```

- When you’re ready to deploy to production, running `npm run build` will create an optimized build of your app in the `build` folder.

## Quick Start

### Creating and nesting components 

- React apps are made out of **components**. 
- A component is a piece of the UI (user interface) that has its own logic and appearance. 
- A component can be as small as a button, or as large as an entire page.
- React components are **JavaScript/TypeScript functions** that return markup
- React **component** names must always **start with a capital letter**, while **HTML tags** must be **lowercase**.
- `export default` keywords specify the **main component in the file**; check [MDN](https://developer.mozilla.org/en-US/docs/web/javascript/reference/statements/export) and [javascript.info](https://javascript.info/import-export)


```tsx
function MyButton() {
  return (
    <button>
      I'm a button
    </button>
  );
}

export default function MyApp() {
  return (
    <div>
      <h1>Welcome to my app</h1>
      <MyButton />
    </div>
  );
}
```

### Writing markup with JSX 
- The markup syntax JSX is optional, but most React projects use JSX for its convenience.
- In JSX the components can’t return multiple JSX tags; you have to wrap them into a shared parent, like a <div>...</div> or an empty <>...</> wrapper (a fragment).

### Adding styles 
- You can specify a CSS class with `className`; it works the same way as the HTML `class` attribute
- Then you write the CSS rules for it in a **separate CSS file**
- Then, to use the styles in your component, you import the CSS file (relative path) at the top of the component file.
- You can also “escape into JavaScript” from JSX attributes, **but you have to use curly braces instead of quotes**.   
  
For example, `className="avatar"` passes the "avatar" string as the CSS class, but `src={user.imageUrl}` reads the `JavaScript` user.imageUrl variable value, and then passes that value as the src attribute:

```jsx
const user = {
  name: 'Hedy Lamarr',
  imageUrl: 'https://i.imgur.com/yXOvdOSs.jpg',
  imageSize: 90,
};

export default function Profile() {
  return (
    <>
      <h1>{user.name}</h1>
      <img
        className="avatar"
        src={user.imageUrl}
        alt={'Photo of ' + user.name}
        style={{
          width: user.imageSize,
          height: user.imageSize
        }}
      />
    </>
  );
}
```

### Conditional rendering 

- You can use an `if` statement to conditionally include JSX
- You can use the conditional `?` operator. Unlike if, **it works inside JSX**
- When you don’t need the `else` branch, you can also use a shorter logical `&&` syntax


### Rendering lists

- You will rely on JavaScript features like `for` loop and the array `map()` function to render lists of components
- Inside your component, use the `map()` function to **transform an array of products into an array of <li> items**
- <li> has a `key` attribute
- For each item in a **list**, you should **pass a string or a number that uniquely identifies that item** among its siblings. 

```tsx
const products = [
  { title: 'Cabbage', isFruit: false, id: 1 },
  { title: 'Garlic', isFruit: false, id: 2 },
  { title: 'Apple', isFruit: true, id: 3 },
];

export default function ShoppingList() {
  const listItems = products.map(product =>
    <li
      key={product.id}
      style={{
        color: product.isFruit ? 'magenta' : 'darkgreen'
      }}
    >
      {product.title}
    </li>
  );

  return (
    <ul>{listItems}</ul>
  );
}
```

### Responding to events 

- You can respond to **events** by declaring **event handler functions** inside your components
- `onClick={handleClick}` has no parentheses at the end (handleClick`()`)! **Do not call the event handler function**: you only need to pass it down. React will call your event handler when the user clicks the button.


### Updating the screen

- Often, you’ll want your component to **“remember” some information and display it**. 
- For example, maybe you want to count the number of times a button is clicked. 
- **To do this, add `state` to your component**.
- First, import `useState` from React
- Then you can **declare a state variable** inside your component
- **You’ll get two things from `useState`: the `current state` (count), and the `function that lets you update it` (setCount).** 
- You can give them any names, but the convention is to write [something, setSomething].
- The value that is passed as an argument to `useState()` is the value with which the variabe (something) is initialized.
- When you want to change state, call setSomething() and pass the new value to it.
- The first time the button is displayed, count will be 0 **because you passed 0 to useState()**.
- If you render the same component multiple times, **each will get its own state**. 


```tsx
import { useState } from 'react';

export default function MyApp() {
  return (
    <div>
      <h1>Counters that update separately</h1>
      <MyButton />
      <MyButton />
    </div>
  );
}

function MyButton() {
  const [count, setCount] = useState(0);

  function handleClick() {
    setCount(count + 1);
  }

  return (
    <button onClick={handleClick}>
      Clicked {count} times
    </button>
  );
}
```


### Using Hooks

- Functions starting with `use` are called **Hooks**. 
- `useState` is a built-in Hook provided by React. 
- You can find other built-in Hooks in this [API reference](https://react.dev/reference/react). 
- You can also write your own Hooks by combining the existing ones.


### Sharing data between components 

![](images/img1.webp) 
- Initially, each MyButton’s count state is 0
![](images/img2.webp) 
- The first MyButton updates its count to 1
  
- Often you’ll need components to share data and always **update together**.
- To make both `MyButton` components display the same count and **update together**, you need to **move the state from the individual buttons “upwards” to the closest component containing all of them**.
- In this example, it is `MyApp`:

![](images/img3.webp) 
- Initially, `MyApp`’s count state is 0 and **is passed down to both children**
![](images/img4.webp) 
- On click, `MyApp` updates its count state to 1 **and passes it down to both children**

- Now when you click either button, the count in `MyApp` will change,**which will change both of the counts in `MyButton`**. 

- As you can see: pass the state down from `MyApp` to each `MyButton`, **together with the shared click handler**. 
- You can pass information to `MyButton` using the JSX curly braces, just like you previously did with built-in tags like <img>

- The information you pass down like this is called `props`. 
- Now the `MyApp` component **contains the count state and the handleClick event handler, and passes both of them down as props to each of the buttons**.
- Finally, change `MyButton` to read the `props` you have passed from its parent component

```tsx
export default function MyApp() {
  // ======= To here =========
  const [count, setCount] = useState(0);

  function handleClick() {
    setCount(count + 1);
  }

  // =========================

  return (
    <div>
      <h1>Counters that update together</h1>
      <MyButton count={count} onClick={handleClick} />
      <MyButton count={count} onClick={handleClick} />
    </div>
  );
}

function MyButton() {
  // ================
  // ... we're moving code from here ...
  // ================
    return (
    <button onClick={onClick}>
      Clicked {count} times
    </button>
  );

}
```


