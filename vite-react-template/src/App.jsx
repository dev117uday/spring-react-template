import { BrowserRouter, Route, Switch } from "react-router-dom";

import ProtectedRoute from "./lib/ProtectedRoute";
import Profile from "./component/Profile";
import Main from "./component/Main";
import NavBar from "./component/NavBar";

function App() {
  return (
    <div>
      <NavBar />
      <BrowserRouter>
        <Switch>
          <Route exact path="/" component={Main} />
          <ProtectedRoute exact path="/profile" component={Profile} />
          <Route path="*" component={() => {
            window.location.href = "/";
          }} />
        </Switch>
      </BrowserRouter>
    </div>
  );
}

export default App;