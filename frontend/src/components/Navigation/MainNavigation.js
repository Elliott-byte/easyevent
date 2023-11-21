import { NavLink } from "react-router-dom";
import "./MainNavigation.css"
import {useContext} from "react";
import {AuthContext} from "../../context/AuthContext";

function MainNavagation() {
	const {auth, setAuth} = useContext(AuthContext);

	return (
		<header className="main-navigation">
			<div className="main-navigation__logo">
				<h1>Easy Event</h1>
			</div>
			<nav className="main-navigation__items">
				<ul>
					{
						!auth && <li><NavLink to="/auth">Authentication</NavLink></li>
					}
					<li><NavLink to="/events">Events</NavLink></li>
					{
						auth && <li><NavLink to="/bookings">Bookings</NavLink></li>
					}

				</ul>
			</nav>
		</header>
	)
}

export default MainNavagation;