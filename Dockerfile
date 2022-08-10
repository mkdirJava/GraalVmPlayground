# Handling building of tools
FROM ghcr.io/graalvm/graalvm-ce:22.2.0 as build
RUN gu install native-image


WORKDIR /app
COPY ./target/graalvm_practice.jar ./app/graalvm_practice.jar
RUN native-image -jar ./app/graalvm_practice.jar --no-fallback --initialize-at-build-time=com.entry.Entry -J--add-modules -JALL-SYSTEM

FROM ghcr.io/graalvm/native-image:22.2.0 as runtime

COPY --from=build /app/graalvm_practice /app/
ENTRYPOINT ["/app/graalvm_practice"]