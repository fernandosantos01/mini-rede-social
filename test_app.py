"""
Test script for the mini social network application
"""
import unittest
from app import create_app, db
from app.models import User, Post

class TestMiniRedeSocial(unittest.TestCase):
    
    def setUp(self):
        """Set up test environment before each test"""
        self.app = create_app()
        self.app.config['TESTING'] = True
        self.app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///test.db'
        self.app.config['WTF_CSRF_ENABLED'] = False
        self.client = self.app.test_client()
        
        with self.app.app_context():
            db.create_all()
    
    def tearDown(self):
        """Clean up after each test"""
        with self.app.app_context():
            db.session.remove()
            db.drop_all()
    
    def test_user_registration(self):
        """Test user registration functionality"""
        with self.app.app_context():
            # Create a new user
            user = User(username='testuser', email='test@example.com')
            user.set_password('password123')
            db.session.add(user)
            db.session.commit()
            
            # Verify user was created
            found_user = User.query.filter_by(username='testuser').first()
            self.assertIsNotNone(found_user)
            self.assertEqual(found_user.email, 'test@example.com')
            self.assertTrue(found_user.check_password('password123'))
            self.assertFalse(found_user.check_password('wrongpassword'))
    
    def test_post_creation(self):
        """Test post creation functionality"""
        with self.app.app_context():
            # Create a user
            user = User(username='testuser', email='test@example.com')
            user.set_password('password123')
            db.session.add(user)
            db.session.commit()
            
            # Create a post
            post = Post(content='Test post content', author=user)
            db.session.add(post)
            db.session.commit()
            
            # Verify post was created
            found_post = Post.query.first()
            self.assertIsNotNone(found_post)
            self.assertEqual(found_post.content, 'Test post content')
            self.assertEqual(found_post.author.username, 'testuser')
    
    def test_follow_unfollow(self):
        """Test follow and unfollow functionality"""
        with self.app.app_context():
            # Create two users
            user1 = User(username='user1', email='user1@example.com')
            user1.set_password('password123')
            user2 = User(username='user2', email='user2@example.com')
            user2.set_password('password123')
            db.session.add(user1)
            db.session.add(user2)
            db.session.commit()
            
            # User1 follows User2
            user1.follow(user2)
            db.session.commit()
            
            # Verify follow relationship
            self.assertTrue(user1.is_following(user2))
            self.assertFalse(user2.is_following(user1))
            self.assertEqual(user1.followed.count(), 1)
            self.assertEqual(user2.followers.count(), 1)
            
            # User1 unfollows User2
            user1.unfollow(user2)
            db.session.commit()
            
            # Verify unfollow
            self.assertFalse(user1.is_following(user2))
            self.assertEqual(user1.followed.count(), 0)
            self.assertEqual(user2.followers.count(), 0)
    
    def test_feed_posts(self):
        """Test feed functionality showing posts from followed users"""
        with self.app.app_context():
            # Create users
            user1 = User(username='user1', email='user1@example.com')
            user1.set_password('password123')
            user2 = User(username='user2', email='user2@example.com')
            user2.set_password('password123')
            user3 = User(username='user3', email='user3@example.com')
            user3.set_password('password123')
            db.session.add_all([user1, user2, user3])
            db.session.commit()
            
            # User1 follows User2
            user1.follow(user2)
            db.session.commit()
            
            # Create posts
            post1 = Post(content='Post by user1', author=user1)
            post2 = Post(content='Post by user2', author=user2)
            post3 = Post(content='Post by user3', author=user3)
            db.session.add_all([post1, post2, post3])
            db.session.commit()
            
            # Get user1's feed (should contain posts from user1 and user2, but not user3)
            feed_posts = user1.get_feed_posts().all()
            post_contents = [p.content for p in feed_posts]
            
            self.assertIn('Post by user1', post_contents)
            self.assertIn('Post by user2', post_contents)
            self.assertNotIn('Post by user3', post_contents)
    
    def test_register_page_loads(self):
        """Test that registration page loads"""
        response = self.client.get('/register')
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'Cadastro', response.data)
    
    def test_login_page_loads(self):
        """Test that login page loads"""
        response = self.client.get('/login')
        self.assertEqual(response.status_code, 200)
        self.assertIn(b'Login', response.data)
    
    def test_user_registration_via_form(self):
        """Test user registration through web form"""
        response = self.client.post('/register', data={
            'username': 'newuser',
            'email': 'newuser@example.com',
            'password': 'password123'
        }, follow_redirects=True)
        
        self.assertEqual(response.status_code, 200)
        
        # Verify user was created in database
        with self.app.app_context():
            user = User.query.filter_by(username='newuser').first()
            self.assertIsNotNone(user)
            self.assertEqual(user.email, 'newuser@example.com')

if __name__ == '__main__':
    print("Running tests for Mini Rede Social...")
    print("-" * 50)
    
    # Run tests
    suite = unittest.TestLoader().loadTestsFromTestCase(TestMiniRedeSocial)
    runner = unittest.TextTestRunner(verbosity=2)
    result = runner.run(suite)
    
    # Print summary
    print("\n" + "=" * 50)
    print(f"Tests run: {result.testsRun}")
    print(f"Successes: {result.testsRun - len(result.failures) - len(result.errors)}")
    print(f"Failures: {len(result.failures)}")
    print(f"Errors: {len(result.errors)}")
    print("=" * 50)
