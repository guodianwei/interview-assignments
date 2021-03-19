const redis = require("redis");
const md5 = require("blueimp-md5");

// redis连接实例
const REDIS_CLINET = redis.createClient({
    host: '127.0.0.1',
    port: 6379
});

// 基础字符数组
const BASE_CHAR_ARRAY = [
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
    'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
    'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
    'y', 'z', '0', '1', '2', '3', '4', '5'];

// 基础网址前缀
const BASE_SHORT_URL_PRE = "https://s.huaxing.com/"

/**
 * 计算生产短链接
 * @param url 长链接
 * @param shortUrlLength 短链接长度
 * @returns 短链接数组
 */
const _makeShortUrl = (url: string, shortUrlLength: number = 6): Array<string> => {
    const md5Url: string = md5(url);
    const md5UrlLength = md5Url.length;
    const md5UrlSubLength = md5UrlLength / 8;         // 划分为4段，保留4组结果
    const resultArray: Array<string> = [];
    for (let i = 0; i < md5UrlSubLength; i++) {
        const subHex = md5Url.substr(i * 8, 8); // 分成4段，每段取8位字符
        let subNumberValue = 0x3FFFFFFF & (1 * Number.parseInt('0x' + subHex));// 8位字节与16进制取与操作
        let itemResult = '';
        for (let j = 0; j < shortUrlLength; j++) {
            const tempVal = 0x0000001F & subNumberValue;               // 取0~31之间的整数
            itemResult = itemResult + BASE_CHAR_ARRAY[tempVal];          // 从数组中获取对应字符
            subNumberValue = subNumberValue >> (30 / shortUrlLength);
        }
        resultArray[i] = itemResult;
    }
    return resultArray;
}

/**
 * 获取redis值
 * @param key redis key
 * @returns
 */
const _getRedisKey = (key: string): any => {
    return new Promise((resove, reject) => {
        REDIS_CLINET.get(key, (err: any, result?: any) => {
            if (err) {
                reject();
            } else {
                resove(result);
            }
        });
    })
}

/**
 * 获取短链接
 * @param sourceUrl 源连接
 * @returns
 */
export const getShortUrl = async (sourceUrl: string): Promise<string> => {
    const shortUrlPost = _makeShortUrl(sourceUrl, 6)[0];
    const shortUrlKey = 'short_url_' + shortUrlPost;

    if (!await _getRedisKey(shortUrlKey)) {
        REDIS_CLINET.set(shortUrlKey, sourceUrl); // 缓存键值关系
    } else {
        console.log("from cache get shortUrl");
    }
    REDIS_CLINET.expire(shortUrlKey, 60 * 60 * 6); // 6小时过期

    return BASE_SHORT_URL_PRE + shortUrlPost;
}

/**
 * 获取源连接
 * @param shortUrl 短链接
 */
export const getSourceUrl = async (shortUrl: string): Promise<string | null> => {
    const shortUrlPost = shortUrl.replace(BASE_SHORT_URL_PRE, "");
    const shortUrlKey = 'short_url_' + shortUrlPost;
    const sourceUrl = await _getRedisKey(shortUrlKey);
    if (!sourceUrl) {
        console.log("短链已过期或无效");
        return null;
    } else {
        return sourceUrl;
    }
}


const testMain = async () => {
    const testSourceUrl = "https://movie.douban.com/subject/1292052";
    console.log('input source url ', testSourceUrl);

    const testShortUrl = await getShortUrl(testSourceUrl);
    console.log('get short Url', testShortUrl);

    const getTestSourceUrl = await getSourceUrl(testShortUrl);
    console.log('get source Url', getTestSourceUrl);
}

testMain();

