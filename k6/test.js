import http from 'k6/http';
import { check, sleep, fail } from 'k6';
import { uuidv4 } from 'https://jslib.k6.io/k6-utils/1.1.0/index.js';

export let options = {
    scenarios: {
        default: {
            executor: 'per-vu-iterations',
            vus: 101,
            iterations: 2,
            maxDuration: '30m30s',
        },
    },
};

const BASE_URL = 'http://localhost:8080';
const users = []; // VU당 고유 사용자 정보를 저장할 배열

export default function () {
    const vuIndex = __VU - 1; // VU는 1부터 시작하므로 0부터 인덱싱

    if (!users[vuIndex]) {
        // Step 1: 회원가입 (첫 반복일 때만 실행)
        const email = `user_${uuidv4()}@example.com`;
        const password = 'password123';

        let registerRes = http.post(`${BASE_URL}/auth/register`, JSON.stringify({
            email: email,
            password: password,
        }), {
            headers: { 'Content-Type': 'application/json' },
        });

        check(registerRes, {
            'register succeeded': (res) => res.status === 200,
        });

        if (registerRes.status !== 200) {
            console.error('Register failed:', registerRes.status, registerRes.body);
            fail('Failed to register.');
        }

        // VU 고유 사용자 정보 저장
        users[vuIndex] = { email: email, password: password };
    }

    const { email, password } = users[vuIndex];

    // Step 2: 로그인
    let loginRes = http.post(`${BASE_URL}/auth/login`, JSON.stringify({
        email: email,
        password: password,
    }), {
        headers: { 'Content-Type': 'application/json' },
    });

    check(loginRes, {
        'login succeeded': (res) => res.status === 200,
    });

    if (loginRes.status !== 200) {
        console.error('Login failed:', loginRes.status, loginRes.body);
        fail('Failed to login.');
    }

    let sessionCookie = loginRes.cookies['JSESSIONID'][0]?.value;

    if (!sessionCookie) {
        fail('Session cookie not found.');
    }

    // Step 3: 쿠폰 발급 요청
    let couponRes = http.post(`${BASE_URL}/apply/coupon`, null, {
        headers: {
            'Content-Type': 'application/json',
            'Cookie': `JSESSIONID=${sessionCookie}`,
        },
    });

    check(couponRes, {
        'coupon applied successfully': (res) => res.status === 200,
    });

    if (couponRes.status !== 200) {
        console.error('Coupon application failed:', couponRes.status, couponRes.body);
    }

    sleep(1);
}
