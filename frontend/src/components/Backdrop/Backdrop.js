import "./Backdrop.css"

export default function Backdrop(props){
    return <div className="backdrop" onClick={props.onClick}></div>
}