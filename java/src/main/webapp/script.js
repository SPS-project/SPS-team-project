
data = {
    "name": "Ankit Verma",
    "age": "20", 
    "email": "verma.ankit484@gmail.com",
    "gender": "MALE"
}

onloadEvents = () => {
    fetch('user', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(res => res.json()).then(res => console.log(res));
}

document.addEventListener("DOMContentLoaded", onloadEvents);