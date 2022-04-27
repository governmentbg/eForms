!#/bin/sh
for D in ./*; do
    cd "$D"
    yarn build
    cd ..
done