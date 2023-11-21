import "./Auth.css"
import {useContext, useState} from "react";
import {AuthContext} from "../context/AuthContext";
function Auth() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [isLogin, setIslogin] = useState(false);
    const {auth, setAuth} = useContext(AuthContext);
    const submitHandle = (e) => {
        e.preventDefault();
        if (email.trim().length === 0 || password.trim().length === 0){
            return;
        }
        console.log(email, password);

        let requestBody = {
            query: `
                query{
                    login(loginInput:{
                        email: "${email}"
                        password: "${password}"
                    }){
                        userEmail
                        token
                        tokenExpiration
                    }
                }
            `
        }
        if(isLogin){
            requestBody = {
                query: `
                mutation{
                    createUser(userInput: {
                        email: "${email}"
                        password: "${password}"
                    }){
                        id
                        email
                    }
                }
            `
            }
        }
        fetch("http://localhost:8080/graphql", {
            method: "POST",
            body: JSON.stringify(requestBody),
            headers:{
                "Context-Type":"application/json"
            }
        }).then((res) => {
            if (res.status !== 200 && res.status !== 201){
                throw new Error("Error!")
            }
            setIslogin(true);
            return res.json();
        }).then(resData => {
            console.log(resData);
            if(resData.data.login){
                setAuth((prevAuth) => {
                    return {
                        ...prevAuth,
                        token: resData.data.login.token,
                        userEmail: resData.data.login.userEmail
                    }
                });
            }
        })
            .catch((e) => console.log(e));
    }
    return (
        <form className="auth-form">
            <div className="form-control">
                <label htmlFor="email">Email</label>
                <input type="email" id="email"
                       onChange={(e) => setEmail(e.target.value)}/>
            </div>
            <div className="form-control">
                <label htmlFor="password">Password</label>
                <input type="password" id="password"
                       onChange={e => setPassword(e.target.value)}/>
            </div>
            <div className="form-actions">
                <button onClick={e => submitHandle(e)}>Submit</button>
                <button type="button" onClick={e => setIslogin(!isLogin)}>Switch to {isLogin ? "Login":"Signup"}</button>
            </div>
        </form>
    )
}
export default Auth;