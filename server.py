from fastapi import FastAPI

app = FastAPI()

counter = 0  # Initializing the counter


@app.get("/")
def get_counter():
    return {"counter": counter}


@app.post("/inc")
def increment_counter():
    global counter
    counter += 1
    return {"message": "Counter incremented successfully"}


if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="0.0.0.0", port=8000)
