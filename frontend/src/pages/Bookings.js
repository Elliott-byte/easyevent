import {Fragment, useContext, useEffect, useState} from "react";
import BookingList from "../components/BookingList/BookingList";
import {AuthContext} from "../context/AuthContext";
import Backdrop from "../components/Backdrop/Backdrop";

function Bookings() {
	const {auth, setAuth} = useContext(AuthContext);
	const [bookings, setBookings] = useState([]);
	const [loading, setLoading] = useState(false);

	// Fetch bookings
	const fetchBooking = () => {
		setLoading(true);
		const requestBody = {
			query: `
			query {
				bookings{
					id
					createdAt
					event{
						id
						title
						date
					}
				}
			}
			 `
		}

		fetch("http://localhost:8080/graphql", {
			method: "POST",
			body: JSON.stringify(requestBody),
			headers: {
				"Context-Type": "application/json",
				"Authorization": "Bearer " + auth.token
			}
		}).then((res) => {
			if (res.status !== 200 && res.status !== 201) {
				throw new Error("Error!")
			}
			return res.json();
		}).then(resData => {
			// console.log(resData);
			const bookings = resData.data.bookings;
			setBookings(bookings);
			setLoading(false);
		}).catch(err => {
			console.log(err);
			setLoading(false);
		})

	}

	useEffect(() => {
		fetchBooking();
	}, []);

	const handleCancle = (bookingId) => {
		setLoading(true);
		const requestBody = {
			query: `
			mutation {
				cancelBooking(bookingId: "${bookingId}"){
					title
					description
				}
			} 
			 `
		};
		fetch("http://localhost:8080/graphql", {
			method: "POST",
			body: JSON.stringify(requestBody),
			headers:{
				"Content-Type": "application/json",
				Authorization: "Bearer " + auth.token
			},
		}).then((res) => {
			if (res.status !== 200 && res.status !== 201) {
				throw new Error("Error!")
			}
			return res.json();
		}).then((resData) => {
			// console.log(resData);
			setLoading(false);
			fetchBooking();
		}).catch((err) => {
			console.log(err);
			setLoading(false);
		})
	}


	return (
		<Fragment>
			{loading && <Backdrop />}
			<BookingList
				bookings={bookings}
				onDelete={handleCancle}
			/>
		</Fragment>
	)
}
export default Bookings;