import babel from "@rollup/plugin-babel";
import resolve from "@rollup/plugin-node-resolve";
import commonjs from "@rollup/plugin-commonjs";
import replace from "@rollup/plugin-replace";
import typescript from '@rollup/plugin-typescript';
import json from '@rollup/plugin-json';

const extensions = ['.js', '.jsx', '.ts', '.tsx'];

export default {
  input: "./src/plugin.js",
  output: {
    file: "dist/plugin.js"
  },
  plugins: [
    resolve(),
    babel({
        presets: [ "@babel/preset-react", ["@babel/preset-typescript", {isTSX: true, allExtensions: true}]],
        exclude: './node_modules',
        babelHelpers: "bundled",
        extensions
    }),
    commonjs({
      include: "node_modules/**"
    }),
    typescript({
        tsconfig: './tsconfig.build.json',
        declaration: true,
        declarationDir: 'dist',
      }),
    replace({
      "process.env.NODE_ENV": JSON.stringify("production")
    }),
    json({
      compact: true,
    }),
  ]
};