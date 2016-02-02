# TEST: completeApplication

## Files

- In `bases` there is a Dockerfile (`ubuntubase`) that generates a base image with __ubuntu 14.04__ + __Puppet__.

- In `build` there is a folder for each service:

  - `redis`: contains a `Dockerfile` to generate an image based on the official __redis__ image.

  - `webproducer`: contains a puppet manifest (`manifest.pp`) to generate a web application. It must use `ubuntubase`

  - `webconsumer`: contains a `Dockerfile` to generate an image based on `ubuntubase` and a `manifest.pp` to generate a web application.

- In `run` there is a `compose.yml` file that is used to deploy and orchestrate the complete application (based on 3 containers).

## Application

It is a simple application based on a redis database and two webs. One web adds one to a field in the database each time it is loaded. The other subtract one to that field.
