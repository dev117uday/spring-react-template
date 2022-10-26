// import react
import React from 'react';
import { useState, useEffect } from 'react';
import { getAuth, signInWithPopup, GoogleAuthProvider, signOut } from "firebase/auth";


function NavBar() {

  const [authState, setAuthState] = useState(false);

  useEffect(() => {
    (localStorage.getItem('isAuth') === 'true') ? setAuthState(true) : setAuthState(false);
  }, []);

  function loginButton() {

    const auth = getAuth();
    const provider = new GoogleAuthProvider();
    provider.addScope('https://www.googleapis.com/auth/userinfo.profile');

    signInWithPopup(auth, provider)
      .then((result) => {
        const credential = GoogleAuthProvider.credentialFromResult(result);

        console.log(credential);

        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");

        var raw = JSON.stringify({
          "idToken": credential.idToken,
          "userName" : result.user.displayName
        });

        var requestOptions = {
          method: 'POST',
          headers: myHeaders,
          body: raw,
          redirect: 'follow'
        };

        fetch(`${import.meta.env.VITE_backend_url}/authenticate`, requestOptions)
          .then(response => response.text())
          .then(result => {
            console.log(result);
            localStorage.setItem('jwtToken', result);
            localStorage.setItem('isAuth', 'true');
            setAuthState(true);
          }).catch(error => {
            console.log('error', error)
            setAuthState(false);
          });

        localStorage.setItem('username', result.user.displayName);
        localStorage.setItem('useremail', result.user.email);
        localStorage.setItem('userphoto', result.user.photoURL);

      }).catch((error) => {
        console.log(error)
        setAuthState(false);
      });
  }

  function logoutButton() {
    localStorage.clear();
    setAuthState(false);
    const auth = getAuth();
    signOut(auth).then(() => {
      console.log("success")
      window.location.href = '/';
    }).catch((error) => {
      console.log("error" + error)
    });
  }

  return (
    <div>

      <nav className="navbar navbar-expand-lg navbar-light bg-light">
        <div className="container-fluid">
          <a className="navbar-brand" href="/">Template</a>
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarSupportedContent">
            <ul className="navbar-nav me-auto mb-2 mb-lg-0">
              {
                authState ?
                  <li className="nav-item">
                    <a className="nav-link active" aria-current="page" href="/profile">Profile</a>
                  </li> : <></>
              }
            </ul>
            {
              authState ?
                <button className="btn btn-danger" onClick={logoutButton}>Logout</button>
                : <button className="btn btn-primary" onClick={loginButton}>Login</button>
            }
          </div>
        </div>
      </nav>
    </div>
  );
}

export default NavBar;
