import "./Events.css"
import Backdrop from "../components/Backdrop/Backdrop";
import {Fragment, useContext, useEffect, useState} from "react";
import Modal from "../components/Modal/Modal";
import {AuthContext} from "../context/AuthContext";
import EventList from "../components/EventList/EventList";

function formatDate(dateString) {
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份是从0开始的
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
}

function Events() {
    const [loading, setLoading] = useState(false);
    const [creating, setCreating] = useState(false);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [canCancel, setCanCancel] = useState(true);
    const [canConfirm, setCanConfirm] = useState(true);
    const [formState, setFormState] = useState({
        title: "",
        price: "",
        date: "",
        description: ""
    })
    const {auth, setAuth} = useContext(AuthContext);
    const [events, setEvents] = useState([]);
    const handleCancle = () => {
        setCreating(false);
        setLoading(false);
    }

    // Fetch events
    const fetchEvents = () => {
        setLoading(true);
        const requestBody = {
            query: `
            query {
                events{
                    id
                    title
                    description
                    date
                    price
                    creator{
                        id
                        email
                    }
                }
            }
             `
        }

        fetch("http://localhost:8080/graphql", {
            method: "POST",
            body: JSON.stringify(requestBody),
            headers: {
                "Content-Type": "application/json"
            },
        }).then(
            (res) => {
                if (res.status !== 200 && res.status !== 201) {
                    throw new Error("Failed!");
                }
                return res.json();
            }
        ).then(
            (resData) => {
                // console.log(resData);
                setEvents(resData.data.events);
                setLoading(false);
            }
        ).catch(
            (err) => {
                console.log(err);
                setLoading(false);
            }
        )
    }
    useEffect(() => {
        fetchEvents();
    }, []);

    // Assign form value to form state
    const handleChange = (e) => {
        const {name, value} = e.target;
        setFormState((prevState) => {
            return {
                ...prevState,
                [name]: value
            }
        })
    }

    // Create event
    const handleCreateEvent = (e) => {
        e.preventDefault();
        console.log(formState);
        if (auth === null) return;
        // "java.time.format.DateTimeParseException: Text '2023-11-21' could not be parsed at index 10"
        const requestBody = {
            query: `
            mutation {
                createEvent(eventInput: 
                {
                title: "${formState.title}", 
                description: "${formState.description}", 
                price: ${formState.price}, 
                date: "${formatDate(formState.date)}"
                }){
                    id
                    title
                    description
                    date
                    price
                    creator{
                        id
                        email
                    }
                }
            }
             `
        };

        fetch("http://localhost:8080/graphql", {
                method: "POST",
                body: JSON.stringify(requestBody),
                headers: {
                    "Content-Type": "application/json",
                    Authorization: "Bearer " + auth.token
                }
            }
        ).then((res) => {
            if (res.status !== 200 && res.status !== 201) {
                throw new Error("Failed!");
            }
            return res.json();
        }).then((resData) => {
            console.log(resData);
            setEvents((prevState) => {
                const updatedEvents = [...prevState];
                updatedEvents.push({
                    id: resData.data.createEvent.id,
                    title: resData.data.createEvent.title,
                    description: resData.data.createEvent.description,
                    date: resData.data.createEvent.date,
                    price: resData.data.createEvent.price,
                    creator: {
                        email: resData.data.createEvent.creator.email
                    }
                });
                return updatedEvents;
            });
        });

        setCreating(false);
        setLoading(false)
    }

    // View event detail
    const handleViewDetail = (eventId) => {
        setCanConfirm(false);
        if (auth) {
            setLoading(true);
            const requestBody = {
                query: `
                query {
                    bookings{
                        event{
                            id
                        }
                    }
                }
                 `
            };
            fetch("http://localhost:8080/graphql", {
                method: "POST",
                body: JSON.stringify(requestBody),
                headers: {
                    "Content-Type": "application/json",
                    Authorization: "Bearer " + auth.token
                }
            }).then((res) => {
                if (res.status !== 200 && res.status !== 201) {
                    throw new Error("Failed!");
                }
                return res.json();
            }).then((resData) => {
                const isBooked = resData.data.bookings.find((booking) => booking.event.id === eventId);
                if (isBooked) {
                    setCanConfirm(false);
                } else {
                    setCanConfirm(true);
                }
                setSelectedEvent(events.find((event) => event.id === eventId));
            });
            setLoading(false);
        } else {
            setCanConfirm(true);
            setSelectedEvent(events.find((event) => event.id === eventId));
        }


    }

    //Book event
    const handleBookEvent = (e) => {
        e.preventDefault();
        if (auth.token === undefined) {
            setSelectedEvent(null);
            setLoading(false);
            return;
        }
        const requestBody = {
            query: `
            mutation {
                bookEvent(eventId: "${selectedEvent.id}"){
                    id
                    createdAt
                    updatedAt
                }
            }
             `
        };

        fetch("http://localhost:8080/graphql", {
            method: "POST",
            body: JSON.stringify(requestBody),
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + auth.token
            },
        }).then((res) => {
            if (res.status !== 200 && res.status !== 201) {
                throw new Error("Failed!");
            }
            return res.json();
        }).then((resData) => {
            // console.log(resData);
            setSelectedEvent(null);
            setLoading(false);
        }).catch((err) => {
            console.log(err);
            setLoading(false);
        })


    }
    return (
        <Fragment>
            {(loading || selectedEvent) && <Backdrop/>}
            {creating && <Modal
                title="Add Event"
                canCancel
                canConfirm
                onCancel={handleCancle}
                onConfirm={handleCreateEvent}
                confirmText="Confirm"
            >
                <form>
                    <div className="form-control">
                        <label htmlFor="title">Title</label>
                        <input name="title" type="text" id="title" onChange={handleChange}/>
                    </div>
                    <div className="form-control">
                        <label htmlFor="price">Price</label>
                        <input name="price" type="number" id="price" onChange={handleChange}/>
                    </div>
                    <div className="form-control">
                        <label htmlFor="date">Date</label>
                        <input name="date" type="date" id="date" onChange={handleChange}/>
                    </div>
                    <div className="form-control">
                        <label htmlFor="description">Description</label>
                        <textarea name="description" rows="4" cols="54" onChange={handleChange}/>
                    </div>
                </form>
            </Modal>
            }

            {auth && <div className="events-control">
                <p>Share your own Events!</p>
                <button className="btn" onClick={() => {
                    setCreating(true);
                    setLoading(true);
                }}>Create Event
                </button>
            </div>
            }
            {selectedEvent && <Modal
                title={selectedEvent.title}
                canCancel={canCancel}
                canConfirm={canConfirm}
                onCancel={() => {
                    setSelectedEvent(null);
                    setLoading(false);
                }}
                onConfirm={(e) => {
                    handleBookEvent(e)
                }}
                confirmText={auth.token ? "Book" : "Confirm"}
            >
                <h1>{selectedEvent.title}</h1>
                <h2>${selectedEvent.price} - {new Date(selectedEvent.date).toLocaleDateString()}</h2>
                <p>{selectedEvent.description}</p>
            </Modal>}

            <EventList
                events={events}
                auth={auth}
                onViewDetail={handleViewDetail}
            />

        </Fragment>
    );
}

export default Events;