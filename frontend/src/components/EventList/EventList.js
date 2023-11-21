import "./EventList.css"
import EventItem from "../EventItem/EventItem";

function EventList(props){
    return (
        <ul className="event__list">
            {props.events.map((event) => {
            return (
                <EventItem
                    key={event.id}
                    eventId={event.id}
                    title={event.title}
                    price={event.price}
                    date={event.date}
                    userEmail={props.auth.userEmail}
                    creatorEmail={event.creator.email}
                    onDetail={props.onViewDetail}
                />
                 )
                })
            }
        </ul>
    )
}

export default EventList;