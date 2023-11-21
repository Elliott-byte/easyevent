import "./EventItem.css"
import {useState} from "react";
function EventItem(props){
    return (
        <li key={props.id} className="events__list-item">
            <div>
                <h1>{props.title}</h1>
                <h2>${props.price} - {new Date(props.date).toLocaleDateString()}</h2>
            </div>
            <div>
                {props.userEmail === props.creatorEmail ?
                    <p>You're the owner of this event.</p> :
                    <button className="btn"
                            onClick={e => props.onDetail(props.eventId)}
                    >View Details</button>}
            </div>
        </li>
    )
}
export default EventItem;