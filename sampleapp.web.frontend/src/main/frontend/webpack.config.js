var path = require('path');

const PATHS = {
    build: path.join(__dirname, '..', '..', '..', 'target', 'classes')
};

module.exports = {
    mode: 'development',
    entry: './src/index.js',
    output: {
        path: PATHS.build,
        filename: 'bundle.js'
    },
    devtool: 'source-map',
    resolve: {
        extensions: ['.js', '.jsx']
    },
    module: {
        rules: [
            {
                test: /\.jsx?$/,
                exclude: /node_modules/,
                use: ['babel-loader?' + JSON.stringify({
                    cacheDirectory: true,
                    presets: ['@babel/preset-react']
                }), 'eslint-loader'],
            },
            {
                test: /\.css$/,
                use: [ { loader: 'style-loader' }, { loader: 'css-loader' } ]
            },
        ]
    }
};
