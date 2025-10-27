from app import create_app

app = create_app()

if __name__ == '__main__':
    import os
    # Only enable debug mode in development, never in production
    debug_mode = os.environ.get('FLASK_ENV') == 'development'
    app.run(debug=debug_mode)
