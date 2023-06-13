## Velocloud Release Process:
In this example we are releasing version 2.0.0

1) Check out `release-2.x`
2) run `mvn versions:set -DnewVersion=2.0.0` 
3) modify `docs/antora.yml` and change the version to `version: '2.0.0'`
4) commit changes `git commit -a -m "v2.0.0-SNAPSHOT -> v2.0.0`
5) tag the release `git tag v2.0.0`
6) push changes `git push && git push --tags`
7) run `mvn versions:set -DnewVersion=2.0.1-SNAPSHOT` 
8) modify `docs/antora.yml` and change the version to `version: '2.0.1-SNAPSHOT'`
9) commit changes `git commit -a -m "v2.0.0 -> v2.0.1-SNAPSHOT`
10) push changes `git push`
