const form = document.getElementById("uploadForm");
const fileInput = document.getElementById("fileInput");
const uploadButton = document.getElementById("uploadButton");

form.addEventListener("submit", (event) => {
    event.preventDefault();
    const file = fileInput.files[0];
    const formData = new FormData();
    formData.append("file", file);

    //  http://localhost:8080/upload
    fetch("/upload", {
        method: "POST",
        body: formData
    })
        .then(response => {
            if (response.ok) {
                alert("File uploaded successfully");
            } else {
                alert("Failed to upload file");
            }
        })
        .catch(error => {
            console.error(error);
            alert("Failed to upload file");
        });
});