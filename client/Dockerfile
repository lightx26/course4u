FROM node:22-alpine3.19

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json for npm install
COPY package*.json ./

# Install dependencies
RUN npm install
# Copy the rest of your project files into the container
COPY . .

#Port
EXPOSE 5173

# Command to run your application
CMD [ "npm", "run", "dev" ]